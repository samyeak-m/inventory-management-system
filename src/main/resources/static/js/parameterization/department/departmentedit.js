window.formConfig ={
    title: "Edit Department ",
    method: "PUT",
    id: "editbranch",
    fetchAPI: "/api/department/department/{id}",
     updateAPI: "/api/department/update/{id}",
    fields: [
        {
            type: "text",
            label: "Department Name:",
            name: "departmentName",
            required: true,
            placeholder: "Enter Name",
            group: 1
        },

    ],
    buttons: [
        { type: "submit", label: "Update", id:"btn-update", position: "bottom", class: "btn btn-sm btn-warning" },
        { type: "button", label: "Cancel", position: "bottom", redirect: "/", class: "btn btn-secondary" }
    ],
}