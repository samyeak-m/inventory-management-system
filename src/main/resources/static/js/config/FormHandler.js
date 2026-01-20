    // ================================
    // FormHandler Class
    // ================================
    class FormHandler {
    constructor() {
        this.formRowCount = 1; // Start from 0
        this.initializeRowCounter(); // Auto-detect existing rows on edit
    }

        // ----------------------------
        // Add a new dynamic row to the form
        // ----------------------------

     initializeRowCounter() {
    const existingRows = document.querySelectorAll('.form-row');

    existingRows.forEach((row, i) => {
        if (!row.dataset.rowIndex) {
            row.dataset.rowIndex = i; // FIX: assign missing index
        }
    });

    this.formRowCount = existingRows.length;
}
        addFormRow(button) {
            const formElement = document.getElementById(window.formConfig.id);

            // Create a container div for the row
            const formRow = document.createElement("div");
            formRow.classList.add("form-row");
            formRow.dataset.rowIndex = this.formRowCount;

            // Loop through all fields and add those in group 1
            window.formConfig.fields.forEach(field => {
                if (field.group === 1) {
                    const fieldGroup = document.createElement("div");
                    fieldGroup.classList.add("form-group");

                    // Create input/select element based on field type
                    let inputElement;
                    if (field.type === "select") {
                        inputElement = document.createElement("select");
                        inputElement.classList.add("form-control");

                        // Placeholder option
                        const placeholderOption = document.createElement("option");
                        placeholderOption.value = "";
                        placeholderOption.textContent = `Select ${field.label}`;
                        placeholderOption.disabled = true;
                        placeholderOption.selected = true;
                        inputElement.appendChild(placeholderOption);

                        // Fetch options dynamically from API if defined
                        if (field.fetchAPI) {
                            fetch(field.fetchAPI)
                                .then(res => res.json())
                                .then(data => {
                                    data.forEach(item => {
                                        const option = document.createElement("option");
                                        option.value = item[field.optionValueKey];
                                        option.textContent = item[field.optionLabelKey];
                                        inputElement.appendChild(option);
                                    });
                                });
                        }
                    } else {
                        inputElement = document.createElement("input");
                        inputElement.type = field.type;
                        inputElement.classList.add("form-control");
                        inputElement.placeholder = field.placeholder || "";
                        if (field.min !== undefined) inputElement.min = field.min;
                    }

                    // Set input name with row index for uniqueness
                    inputElement.name = `${field.name}_${this.formRowCount}`;
                    inputElement.required = !!field.required;
                    fieldGroup.appendChild(inputElement);

                    // Add an error div (hidden by default)
                    const errorDiv = document.createElement("div");
                    errorDiv.classList.add("field-error");
                    errorDiv.style.display = "none";
                    fieldGroup.appendChild(errorDiv);

                    formRow.appendChild(fieldGroup);
                }
            });

            // Add a remove button for the row
            const removeGroup = document.createElement("div");
            removeGroup.classList.add("form-group");
            const removeBtn = document.createElement("button");
            removeBtn.type = "button";
            removeBtn.textContent = "X";
            removeBtn.classList.add("btn", "btn-danger", "ml-2");
            removeBtn.onclick = () => formRow.remove();
            removeGroup.appendChild(removeBtn);
            formRow.appendChild(removeGroup);

        const buttonContainer = button.parentElement;
            buttonContainer.parentElement.insertBefore(formRow, buttonContainer);

            this.formRowCount++;
        }

        // ----------------------------
        // Handle form submission
        // ----------------------------
        async handleSubmit(e, formElement, formConfig, form, showNotification) {
            e.preventDefault();

            const fieldValidation = new FieldValidation();
            const hasError = fieldValidation.setupRequiredFieldListeners(formElement, formConfig, form, showNotification);
            if (hasError) return;

            const formData = new FormData(formElement);
            const data = {};

            // Process all fields (including aftertableFields)
            const allFields = [...(formConfig.fields || []), ...(formConfig.aftertableFields || [])];
            allFields.forEach(field => this.processField(field, formElement, formData, data));

            // Process tables (if any)
            if (formConfig.tables) {
                formConfig.tables.forEach(table => {
                    const tableElement = document.getElementById(table.name);
                    if (tableElement) {
                        const tableData = [];
                        const rows = tableElement.querySelectorAll('tbody tr');
                        rows.forEach(row => {
                            const rowData = {};
                            table.fields.forEach((field, index) => {
                                const cell = row.cells[index + 1];
                                const input = cell.querySelector('input, select, textarea');
                                if (input) {
                                    rowData[field.name] = input.type === 'checkbox' ? input.checked : input.value;
                                }
                            });
                            if (Object.values(rowData).some(val => val !== '' && val !== false && val !== null && val !== undefined)) {
                                tableData.push(rowData);
                            }
                        });
                        data[table.name] = tableData;
                    }
                });
            }

            // ----------------------------
            // Collect dynamic rows for group 1 fields
            // ----------------------------
        const dynamicItems = [];
    const rows = formElement.querySelectorAll('.form-row[data-row-index]');
   rows.forEach(row => {
    const rowIndex = row.dataset.rowIndex;
    const rowData = {};
    let hasValue = false;

    formConfig.fields.forEach(field => {
        if (field.group === 1) {
           let input =
    row.querySelector(`[name="${field.name}_${rowIndex}"]`) ||
    row.querySelector(`[name="${field.name}"]`);
            if (input) {
                let value = input.value;

                if (input.type === 'checkbox') value = input.checked;

                rowData[field.name] = value;

                if (value != null && String(value).trim() !== "") {
                    hasValue = true;
                }
            }
        }
    });

    // FIX — include row 0 OR any row with values
    if (rowIndex == 0 || hasValue) dynamicItems.push(rowData);
});

            // ----------------------------
            // Dynamic rows: always send as an array to backend
            // Optionally support sending each item separately with `savePerItem`.
            // ----------------------------
            const arrayKey = formConfig.dynamicArrayKey || 'items';
            if (dynamicItems.length > 0) {
                // Attach the array by default
                data[arrayKey] = dynamicItems;
            }

            // If configured to save each dynamic row individually, do that instead
            if (formConfig.savePerItem && dynamicItems.length > 0) {
                // Prepare base data (copy other non-dynamic fields)
                const baseData = Object.assign({}, data);
                delete baseData[arrayKey];

                // Decide the key name for the single item payload
                const singleKey = formConfig.singleItemKey || 'item';

                try {
                    const method = (formConfig.method || 'post').toLowerCase();
                    // Sequentially save each item so backend can assign its own id
                    for (let i = 0; i < dynamicItems.length; i++) {
                        const itemPayload = Object.assign({}, baseData);
                        itemPayload[singleKey] = dynamicItems[i];
                        await ApiService[method](formConfig.submitAPI, itemPayload);
                    }

                    if (typeof showNotification === 'function') {
                        showNotification({ type: 'success', message: 'Saved successfully!', duration: 3000 });
                    } else {
                        alert('Saved successfully!');
                    }

                    if (formConfig.redirectURL) {
                        setTimeout(() => window.location.replace(formConfig.redirectURL), 600);
                    } else {
                        setTimeout(() => window.location.reload(), 600);
                    }

                    if (formConfig.resetAfterSubmit) {
                        formElement.reset();
                        Object.keys(data).forEach(key => delete form[key]);
                    }

                    return; // we've already saved per-item
                } catch (err) {
                    console.error('Error saving items individually:', err);
                    if (typeof showNotification === 'function') {
                        showNotification({ type: 'error', message: `Save failed: ${err.message || 'Unknown error'}`, duration: 5000 });
                    } else {
                        alert(`Save failed: ${err.message || 'Unknown error'}`);
                    }
                    return;
                }
            }

            // ----------------------------
            // Merge numeric-indexed top-level keys ("0","1",...) into the items array.
            // This handles payloads where earlier code or inputs produced numbered keys
            // at top-level — we move them into `data[arrayKey]` so backend receives
            // everything under the expected array key.
            // ----------------------------
            (function mergeNumericKeysIntoArray() {
                const numericKeys = Object.keys(data).filter(k => /^\d+$/.test(k));
                if (numericKeys.length > 0) {
                    data[arrayKey] = data[arrayKey] || [];
                    // Ensure we append in numeric order
                    numericKeys.sort((a, b) => Number(a) - Number(b)).forEach(k => {
                        const val = data[k];
                        if (val !== undefined) data[arrayKey].push(val);
                        delete data[k];
                    });
                }
            })();

            // Also merge numeric keys from the `form` helper object if it exists
            (function mergeNumericKeysFromFormObj() {
                if (!form || typeof form !== 'object') return;
                const numericKeys = Object.keys(form).filter(k => /^\d+$/.test(k));
                if (numericKeys.length === 0) return;
                data[arrayKey] = data[arrayKey] || [];
                numericKeys.sort((a, b) => Number(a) - Number(b)).forEach(k => {
                    const val = form[k];
                    if (val !== undefined) data[arrayKey].push(val);
                    try { delete form[k]; } catch (e) { /* ignore */ }
                });
            })();

            // Debug: show final payload before sending (helps trace remaining numeric keys)
            if (window && window.console && window.console.debug) {
                try {
                    console.debug('Final submission payload for', formConfig.submitAPI, data);
                } catch (e) { /* ignore */ }
            }

            // If backend expects a top-level JSON array (e.g. List<CategoryDto>),
            // send the `dynamicItems` array directly as the request body.
            // Use formConfig.sendArrayAsRoot = true to enable this behavior.
            if (formConfig.sendArrayAsRoot && dynamicItems.length > 0) {
                try {
                    const method = (formConfig.method || 'post').toLowerCase();
                    const result = await ApiService[method](formConfig.submitAPI, dynamicItems);

                    if (typeof showNotification === 'function') {
                        showNotification({ type: 'success', message: result.message || 'Saved successfully!', duration: 3000 });
                    } else {
                        alert(result.message || 'Saved successfully!');
                    }

                    if (formConfig.redirectURL) {
                        setTimeout(() => window.location.replace(formConfig.redirectURL), 600);
                    } else {
                        setTimeout(() => window.location.reload(), 600);
                    }

                    if (formConfig.resetAfterSubmit) {
                        formElement.reset();
                        Object.keys(data).forEach(key => delete form[key]);
                    }

                    return;
                } catch (err) {
                    console.error('Error submitting array as root:', err);
                    if (typeof showNotification === 'function') {
                        showNotification({ type: 'error', message: `Save failed: ${err.message || 'Unknown error'}`, duration: 5000 });
                    } else {
                        alert(`Save failed: ${err.message || 'Unknown error'}`);
                    }
                    return;
                }
            }

            // ----------------------------
            // Optional: send items as indexed top-level keys (e.g. item0, item1)
            // Use `formConfig.sendAsIndexedObject = true` and optionally
            // `formConfig.indexedKeyPrefix = 'item'` to enable.
            // Backend must accept a map-like object or DTO with those fields.
            // ----------------------------
            if (formConfig.sendAsIndexedObject && dynamicItems.length > 0) {
                const prefix = formConfig.indexedKeyPrefix || 'item';
                const indexedObj = {};
                dynamicItems.forEach((it, idx) => {
                    indexedObj[`${prefix}${idx}`] = it;
                });

                try {
                    const method = (formConfig.method || 'post').toLowerCase();
                    const result = await ApiService[method](formConfig.submitAPI, indexedObj);

                    if (typeof showNotification === 'function') {
                        showNotification({ type: 'success', message: result.message || 'Saved successfully!', duration: 3000 });
                    } else {
                        alert(result.message || 'Saved successfully!');
                    }

                    if (formConfig.redirectURL) {
                        setTimeout(() => window.location.replace(formConfig.redirectURL), 600);
                    } else {
                        setTimeout(() => window.location.reload(), 600);
                    }

                    if (formConfig.resetAfterSubmit) {
                        formElement.reset();
                        Object.keys(data).forEach(key => delete form[key]);
                    }

                    return;
                } catch (err) {
                    console.error('Error submitting indexed object:', err);
                    if (typeof showNotification === 'function') {
                        showNotification({ type: 'error', message: `Save failed: ${err.message || 'Unknown error'}`, duration: 5000 });
                    } else {
                        alert(`Save failed: ${err.message || 'Unknown error'}`);
                    }
                    return;
                }
            }

            // ----------------------------
            // Optional: send items as numeric-indexed top-level keys (e.g. "0", "1")
            // Use `formConfig.sendAsNumericIndexObject = true` to enable.
            // Backend must accept a map-like object (e.g. Map<String, CategoryDto>)
            // or an appropriate DTO – this will NOT be a JSON array.
            // ----------------------------
            if (formConfig.sendAsNumericIndexObject && dynamicItems.length > 0) {
                const numericObj = {};
                dynamicItems.forEach((it, idx) => {
                    numericObj[String(idx)] = it;
                });

                try {
                    const method = (formConfig.method || 'post').toLowerCase();
                    const result = await ApiService[method](formConfig.submitAPI, numericObj);

                    if (typeof showNotification === 'function') {
                        showNotification({ type: 'success', message: result.message || 'Saved successfully!', duration: 3000 });
                    } else {
                        alert(result.message || 'Saved successfully!');
                    }

                    if (formConfig.redirectURL) {
                        setTimeout(() => window.location.replace(formConfig.redirectURL), 600);
                    } else {
                        setTimeout(() => window.location.reload(), 600);
                    }

                    if (formConfig.resetAfterSubmit) {
                        formElement.reset();
                        Object.keys(data).forEach(key => delete form[key]);
                    }

                    return;
                } catch (err) {
                    console.error('Error submitting numeric-index object:', err);
                    if (typeof showNotification === 'function') {
                        showNotification({ type: 'error', message: `Save failed: ${err.message || 'Unknown error'}`, duration: 5000 });
                    } else {
                        alert(`Save failed: ${err.message || 'Unknown error'}`);
                    }
                    return;
                }
            }

            // ----------------------------
            // If form has image, use multipart submission
            // ----------------------------
            if (formConfig.image) {
                await this.insertdata(formElement, formConfig, showNotification);
                return;
            }

            // ----------------------------
            // Normal API submission
            // ----------------------------
            try {
                const method = formConfig.method.toLowerCase();
                const result = await ApiService[method](formConfig.submitAPI, data);

                if (typeof showNotification === 'function') {
                    showNotification({
                        type: "success",
                        message: result.message || "Saved successfully!",
                        duration: 3000
                    });
                } else {
                    alert(result.message || "Saved successfully!");
                }

                // Redirect or reload
                if (formConfig.redirectURL) {
                    setTimeout(() => window.location.replace(formConfig.redirectURL), 600);
                } else {
                    // setTimeout(() => window.location.reload(), 600);
                }

                // Optional form reset
                if (formConfig.resetAfterSubmit) {
                    formElement.reset();
                    Object.keys(data).forEach(key => delete form[key]);
                }
            } catch (err) {
                console.error("Error submitting form:", err);
                if (typeof showNotification === 'function') {
                    showNotification({
                        type: "error",
                        message: `Save failed: ${err.message || "Unknown error"}`,
                        duration: 5000
                    });
                } else {
                    alert(`Save failed: ${err.message || "Unknown error"}`);
                }
            }
        }

        // ----------------------------
        // Process individual fields
        // ----------------------------
        processField(field, formElement, formData, data) {
            // Nested groups
            if (field.type === "group" && Array.isArray(field.fields)) {
                field.fields.forEach(subField => this.processField(subField, formElement, formData, data));
                return;
            }

            // Field types
            if (field.type === "checkbox") {
                const checkboxElement = formElement.querySelector(`[name="${field.name}"]`);
                data[field.name] = checkboxElement?.checked || false;
            } else if (field.type === "checkbox-group") {
                const checkedBoxes = formElement.querySelectorAll(`input[name="${field.name}"]:checked`);
                data[field.name] = Array.from(checkedBoxes).map(cb => cb.value);
            } else if (field.type === "radioGroup") {
                const checkedRadio = formElement.querySelector(`input[name="${field.name}"]:checked`);
                data[field.name] = checkedRadio ? checkedRadio.value : '';
            } else if (field.type === "tagInput") {
                // Hidden input for tagInput contains a JSON array string (e.g. "[\"CAT001\",\"CAT002\"]").
                // Parse it so backend receives a real array instead of a JSON string.
                const raw = formData.get(field.name);
                try {
                    data[field.name] = raw ? JSON.parse(raw) : [];
                } catch (e) {
                    // Fallback: if it's not valid JSON, send as single-value array when non-empty
                    data[field.name] = raw ? [raw] : [];
                }
            } else {
                data[field.name] = formData.get(field.name);
            }
        }

        // ----------------------------
        // Setup live image preview
        // ----------------------------
    setupImagePreview(formElement, fieldName, imgPreviewId) {
    const fileInput = formElement.querySelector(`input[type="file"][name="${fieldName}"]`);
    const imgPreview = document.getElementById(imgPreviewId);

    if (!fileInput || !imgPreview) {
        console.warn("Image preview setup failed:", fieldName);
        return;
    }

    fileInput.addEventListener("change", () => {
        const file = fileInput.files?.[0];
        if (!file || !file.type.startsWith("image/")) {
            imgPreview.style.display = "none";
            return;
        }

        imgPreview.src = URL.createObjectURL(file);
        imgPreview.style.display = "block";
    });
}

        // ----------------------------
        // Submit form with multipart/form-data (for image uploads)
        // ----------------------------
        async insertdata(formElement, formConfig, showNotification) {
            const formData = new FormData(formElement);
            try {
                const result = await ApiService.post(formConfig.submitAPI, formData, "multipart/form-data");
                if (typeof showNotification === 'function') {
                    showNotification({
                        type: "success",
                        message: result.message || "Saved successfully!",
                        duration: 3000
                    });
                } else {
                    alert("✅ Saved successfully!");
                }
            } catch (err) {
                console.error("❌ Error submitting form:", err);
                if (typeof showNotification === 'function') {
                    showNotification({
                        type: "error",
                        message: `Save failed: ${err.message || "Unknown error"}`,
                        duration: 5000
                    });
                } else {
                    alert(`❌ Save failed: ${err.message || "Unknown error"}`);
                }
            }
        }
    }

    // ----------------------------
    // Expose the FormHandler instance globally
    // ----------------------------
    const formHandlerInstance = new FormHandler();
    window.addFormRow = function(button) {
        formHandlerInstance.addFormRow(button);
    };
    window.FormHandler = FormHandler;
