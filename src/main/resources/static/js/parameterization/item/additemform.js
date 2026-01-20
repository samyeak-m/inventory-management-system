window.formConfig = {
    title: "Add New Item",
    method: "POST",
    id: "additemform",
    submitAPI: "/api/item/submit-item",
     sendArrayAsRoot: true,
    // Move any top-level group-1 fields into the items array before sending
    forceMoveGroup1ToItems: true,
    redirectURL: "/itemdetails",
    fields: [
        {
            type: "select",
            label: "Category",
            name: "categoryId",
            fetchAPI: "/api/category/category-list",
            required: true,
            placeholder: "Enter Category",
            group: 1,
            optionLabelKey: "categoryName",
            optionValueKey: "categoryId",        },
        {
            type: "text",
            label: "Item Name",
            name: "itemName",
            required: true,
            placeholder: "Enter Item Name",
            group: 1
        },
        {
            type: "text",
            label: "Unit",
            name: "unit",
            required: true,
            placeholder: "Enter Unit",
            group: 1
        },
        {
            type: "text",
            label: "Location",
            name: "location",
            required: true,
            placeholder: "Enter Location",
            group: 1
        },
        {
            type: "number",
            label: "Minimum Order Level",
            name: "minimumOrderLevel",
            required: true,
            placeholder: "Enter Minimum Order Level",
            min: 0,
            group: 1
        },
        {
            type: "number",
            label: "Reorder Level",
            name: "reorderLevel",
            required: true,
            placeholder: "Enter Reorder Level",
            min: 0,
            group: 1
        },

    ],
    buttons: [
        { type: "button", label: "Add Row", position: "bottom", class: "btn btn-dark", onclick: "addFormRow( this)" },
        {type: "submit", label: "Save", position: "bottom", class: "btn btn-sm btn-warning"},
        {type: "button", label: "Cancel", position: "bottom", redirect: "/", class: "btn btn-secondary"}
    ],

}

