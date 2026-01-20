window.formConfig = {
    title: "Branch Setup",
    method: "POST",
    submitAPI: "/api/branch/setup",
    image: true,
    redirectURL: "/branchdetails",
    fields: [
        {
            type: "text",
            label: "Branch Name:",
            name: "branchName",
            required: true,
            placeholder: "Enter Name",
            group: 1
        },
        // {
        //     type: "select",
        //     label: "Branch Code",
        //     name: "branchTypeCode",
        //     required: true,
        //     group: 1,
        //     fetchAPI: "/api/branch/types",
        //     optionLabelKey: "codeName",
        //     optionValueKey: "code"
        // },
        {
            type: "email",
            label: "Email Address:",
            name: "branchEmail",
            required: true,
            placeholder: "e.g. abc@gmail.com",
            group: 1
        },
        {
            type: "text",
            label: "Country:",
            name: "country",
            required: true,
            placeholder: "Enter Country",
            group: 2
        },
        {
            type: "text",
            label: "Province:",
            name: "province",
            required: true,
            placeholder: "Enter Province",
            group: 2
        },
        {
            type: "text",
            label: "City:",
            name: "city",
            required: true,
            placeholder: "Enter City",
            group: 2
        },
        {
            type: "text",
            label: "Address:",
            name: "address",
            required: true,
            placeholder: "Enter Address",
            group: 2
        },
        {
            type: "text",
            label: "Branch Manager:",
            name: "branchManager",
            required: true,
            placeholder: "Enter Branch Manager",
            group: 3
        },
        {
            type: "tel",
            label: "Mobile:",
            name: "branchPhone",
            required: true,
            pattern: "^\\d{6,15}(,\\s*\\d{6,15})*$",
            placeholder: "e.g. 9860112233",
            group: 3
        },
        {
            type: "tel",
            label: "Alternate Mobile:",
            name: "branchAlternatePhone",
            required: false,
            pattern: "^\\d{6,15}(,\\s*\\d{6,15})*$",
            placeholder: "e.g. 9860112233",
            group: 3
        },
        {
            type: "text",
            label: "Contact Person Name:",
            name: "contactPersonName",
            required: false,
            placeholder: "Contact Person Name",
            group: 4
        },
        {
            type: "tel",
            label: "Contact Person Mobile:",
            name: "contactPersonPhone",
            required: false,
            pattern: "^\\d{6,15}(,\\s*\\d{6,15})*$",
            placeholder: "e.g. 9860112233",
            group: 4
        },
        {
            type: "checkbox",
            label: "Status",
            name: "display",
            required: false,
            text: "Active",
            group: 5
        }
    ],
    buttons: [
        { type: "submit", label: "Save", position: "bottom", class: "btn btn-sm btn-warning" },
        { type: "button", label: "Cancel", position: "bottom", redirect: "/", class: "btn btn-secondary" }
    ]
};

