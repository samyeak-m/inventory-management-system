window.formConfig = {
    title: "Add Category",
    id: "itemcategoryform",
    submitAPI: "/api/category/category-save",
    method: "POST",
    sendArrayAsRoot: true,
    forceMoveGroup1ToItems: true,
    redirectURL: "/categorydetails",
    fields: [
        {
            type: "select",
            label: "Category Type",
            name: "categoryTypeId",
            group: 1,
            fetchAPI: "/api/category/categorytypes",
            optionLabelKey: "categoryname",
            optionValueKey: "ctid",
        },
        {
            type: "text",
            label: "Category Name",
            name: "categoryName",
            group: 1,
            required: true,

        },
    ],
    buttons: [{
        type: "button",
        label: "Add Row",
        position: "bottom",
        class: "btn btn-dark",
        onclick: "addFormRow( this)"
    },
        {
            type: "submit",
            label: "Save",
            id: "btn-save",
            position: "bottom",
            class: "btn btn-sm btn-warning",

        },

        {type: "button", label: "Cancel", position: "bottom", redirect: "/categorydetails", class: "btn btn-secondary"}
    ]
}



