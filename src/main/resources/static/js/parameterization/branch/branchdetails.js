document.addEventListener("DOMContentLoaded", function () {
    const columnConfig = [
        { header: "Branch Code", dataKey: "branchCode", resizable: true },
        { header: "Name", dataKey: "branchName", resizable: true },
        {
            header: "Address", dataKey: "cityName", resizable: true,
            renderCell: (row, cellValue, idx) => {
                return [
                    row.addressName,
                    row.cityName,
                    row.stateName,
                    row.countryName
                ]
                    .filter(Boolean)
                    .join(", ");
            }
        },
        { header: "Mobile", dataKey: "branchPhone", resizable: true },
        { header: "Email", dataKey: "branchEmail", resizable: true },
        { header: "Actions", },
    ];



    new TableManager({
        apiEndpoint: "/api/branch/list",
        idKey: "branchId",
        columnConfig: columnConfig,
        confirmMessage: "Are you sure you want to delete this branch?",
        errorMessage: "Failed to delete branch: ",
        actions: [
            { label: "Edit", class:"action-btn edit-btn", type: "url", url: "/branchedit?id={id}" },
            { label: "View", class:"action-btn delete-btn", type: "url", url: "/branchview?id={id}" },
            { label: "Delete", class:"action-btn delete-btn", type: "api", endpoint: "/api/branch/delete/{id}" }
        ]
    });

});


