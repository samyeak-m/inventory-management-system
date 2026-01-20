window.formConfig = {
    title: "Edit Category",
    id: "editcategoryform",
    updateAPI: "/api/category/category-update/{id}",
    fetchAPI: "/api/category/category/{id}",
    method: "PUT",
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
            valuePath: "categoryTypeId.ctid",
            isNumeric: true
        },
        {
            type: "text",
            label: "Category Name",
            name: "categoryName",
            required: true,
            group: 1
        }
    ],
    buttons: [
        {
            type: "submit",
            label: "Update",
            id: "btn-update",
            position: "bottom",
            class: "btn btn-sm btn-warning",
        },
        {type: "button", label: "Cancel", position: "bottom", redirect: "/categorydetails", class: "btn btn-secondary"}
    ]
};