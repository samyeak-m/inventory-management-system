window.formConfig = {
    title: "Category Hierarchy",
    method: "POST",
    submitAPI: "/api/categorytype/save",
    sendArrayAsRoot: true,
    forceMoveGroup1ToItems: true,
    redirectURL: "/categorydetails",
    fields: [
        {
            type: "datalist",
            label: "Category",
            name: "category",
            required: true,
            placeholder: "Enter or select category",
            datalistId: "categorySuggestions",
            options: [],
            group: 1
        },
        {
            type: "text",
            label: "Sub-Category",
            name: "categoryType",
            required: true,
            placeholder: "Enter category type",
            group: 1
        }
    ],
    buttons: [
        {
            type: "button",
            label: "Add Row",
            position: "bottom",
            class: "btn btn-dark",
            onclick: "addFormRow( this)"
        },
        {type: "submit", label: "Save", position: "bottom", class: "btn btn-sm btn-warning"},
        {type: "button", label: "Cancel", position: "bottom", redirect: "/categorydetails", class: "btn btn-secondary"}
    ],
}

