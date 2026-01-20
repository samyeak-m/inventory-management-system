window.formConfig = {
    title: "View Item",
    method: "POST",
    fetchAPI: "/api/item/get-item/{id}",
    fields: [
        {
            type: "text",
            label: "Category",
            name: "categoryId",
            group: 1,      
            readonly: true        },
        {
            type: "text",
            label: "Item Name",
            name: "itemName",
            placeholder: "Enter Item Name",
            group: 1,
            readonly: true
        },
          {
            type: "text",
            label: "Unit",
            name: "unit",
            placeholder: "Enter Unit",
            group: 1,
            readonly: true
        },
        {
            type: "text",
            label: "Location",
            name: "location",
            placeholder: "Enter Location",
            group: 1,
            readonly: true
        },
        {
            type: "number",
            label: "Minimum Order Level",
            name: "minimumOrderLevel",
            placeholder: "Enter Minimum Order Level",
            min: 0,
            group: 1,
            readonly: true
        },
        {
            type: "number",
            label: "Reorder Level",
            name: "reorderLevel",
            placeholder: "Enter Reorder Level",
            min: 0,
            group: 1,
            readonly: true
        },
    ],
    buttons: [
        {type: "button", label: "Back", position: "bottom", redirect: "/itemdetails", class: "btn btn-secondary"}
    ],

}