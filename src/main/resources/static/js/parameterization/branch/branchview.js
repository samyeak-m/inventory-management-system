window.formConfig ={
    title: "View Branch Setup",
    id: "viewbranch",
    fetchAPI: "/api/branch/branch/{id}",
    fields: [
        {
            type: "text",
            label: "Branch Name:",
            name: "branchName",
            group: 1,
            readonly: true
        },
        {
            type: "text",
            label: "Branch Code",
            name: "branchCode",
            required: true,
            group: 1,
            readonly: true
        },
        {
            type: "email",
            label: "Email Address:",
            name: "branchEmail",
            group: 1,
            readonly: true
        },
        {
            type: "text",
            label: "Country:",
            name: "countryName",
            group: 2,
            valuePath:"countryName",
            isNumeric: true,
            readonly: true
        },
        {
            type: "text",
            label: "Province:",
            name: "stateName",
            valuePath:"stateName",
            group: 2,
            readonly: true
        },
        {
            type: "text",
            label: "City:",
            name: "cityName",
            valuePath:"cityName",
            group: 2,
            readonly: true
        },
        {
            type: "text",
            label: "Address:",
            name: "addressName",
            valuePath:"addressName",
            group: 2,
            readonly: true
        },
        {
            type: "text",
            label: "Branch Manager:",
            name: "branchManager",
            group: 3,
            readonly: true
        },
        {
            type: "tel",
            label: "Mobile:",
            name: "branchPhone",
            pattern: "^\\d{6,15}(,\\s*\\d{6,15})*$",
            group: 3,
            readonly: true
        },
        {
            type: "tel",
            label: "Alternate Mobile:",
            name: "branchAlternatePhone",
            required: false,
            pattern: "^\\d{6,15}(,\\s*\\d{6,15})*$",
            group: 3,
            readonly: true
        },

        {
            type: "text",
            label: "Contact Person Name:",
            name: "contactPersonName",
            required: false,
            group: 4,
            readonly: true
        },
        {
            type: "tel",
            label: "Contact Person Mobile:",
            name: "contactPersonPhone",
            required: false,
            pattern: "^\\d{6,15}(,\\s*\\d{6,15})*$",
            group: 4,
            readonly: true
        },

    ],
    buttons: [
              { type: "button", label: "Back", position: "bottom", redirect: "/branchdetails", class: "btn btn-secondary" }
    ],

}

