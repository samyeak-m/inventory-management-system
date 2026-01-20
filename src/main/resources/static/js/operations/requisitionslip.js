window.formConfig = {
    title: "Requisition Slip",
    method: "POST",
    submitAPI: "/",
    fields: [
        {
            type: "text",
            label: "Requisition No.",
            name: "requisitionNo",
            required: true,
            pattern: "^[A-Z]{3}-\\d{4}$", // Example: ABC-1234
            patternError: "Format must be AAA-1234",
            group: 1
        },
        {
            type: "date",
            label: "Date",
            name: "date",
            group: 1
        },
        {
            type: "select",
            label: "Requested By",
            name: "requestedBy",
            fetchAPI: "/api/access/employees-list",
            optionLabelKey: "firstname + ' ' + middleName + ' ' + lastName",
            optionValueKey: "employeeId",
            group: 1
        },
        {
            type: "select",
            label: "Department",
            name: "departmentName",
            optionLabelKey: "departmentName",
            optionValueKey: "departmentId",
            fetchAPI: "/api/department/department-list",

            group: 1
        },
        {
            type: "select",
            label: "Branch",
            name: "branch",
            group: 1,
            optionLabelKey: "branchName",   
            optionValueKey: "branchId",

            fetchAPI: "/api/branch/list",
        }
    ],
    tables: [
        {
            name: "requisitionItems",
            id: "requisitionItems",
            label: "Requisition Items",
            headers: [
                { label: "S.N." },
                { label: "Item Name", required: true },
                { label: "Qty", required: true },
                { label: "Unit", required: true },
                { label: "Remarks" },
                { label: "Action" }
            ],
            fields: [
                { type: "text", name: "itemName", placeholder: "Item Name", required: true },
                { type: "number", name: "qty", placeholder: "Qty", required: true },
                { type: "text", name: "unit", placeholder: "Unit", required: true },
                { type: "text", name: "remarks", placeholder: "Remarks" }
            ]
        }
    ],
    aftertableFields: [
        {
            type: "text",
            label: "Approved By",
            name: "approvedBy",
            group: 3
        },
        {
            type: "text",
            label: "Checked By",
            name: "checkedBy",
            group: 3
        },
        {
            type: "text",
            label: "Prepared By",
            name: "preparedBy",
            group: 3
        },
        {
            type: "text",
            label: "Authorized By",
            name: "authorizedBy",
            group: 3
        }
    ],
    buttons: [
        { type: "button", label: "Add Row", position: "table", class: "btn btn-dark", onclick: "addRowByButton(this)", "data-table-id": "requisitionItems" },
        { type: "submit", label: "Save", position: "bottom", class: "btn btn-sm btn-warning" },
        { type: "button", label: "Cancel", position: "bottom", redirect: "/", class: "btn btn-secondary" }
    ],
}
