document.addEventListener("DOMContentLoaded", function () {
    const columnConfig = [
        {
            header: "Name",
            dataKey: "firstName",
            resizable: true,
            sortable: true,
            renderCell: (row, cellValue, idx) => {
                return [row.firstname, row.middleName, row.lastName]
                    .filter(Boolean)
                    .join(" ");
            }
        },
        { header: "Type", dataKey: "type", resizable: true, sortable: true },
        { header: "Email", dataKey: "email", resizable: true, sortable: true },
        { header: "Mobile", dataKey: "mobile", resizable: true, sortable: true },
        {
            header: "Branch",
            resizable: true,
            sortable: true,
            renderCell: (row) => {
                return row.branch?.branchName || row.branch || "";
            }
        },
        {
            header: "Department",
            resizable: true,
            sortable: true,
            renderCell: (row) => {
                return row.department?.departmentName || row.department || "";
            }
        },
        {
            header: "Photo",
            renderCell: (item) => `<img src="${item.picture}" class="staffimg" alt="photo">`
        },
        {
            header: "Action",
        }
    ];

    new TableManager({
        apiEndpoint: "/api/access/employees-list",
        idKey: "employeeId",
        columnConfig: columnConfig,
        confirmMessage: "Are you sure you want to delete this user?",
        errorMessage: "Failed to delete user: ",
        actions: [
            { label: "Edit", class:"action-btn edit-btn", type: "url", url: "/companystaffedit?id={id}" },
            { label: "View", class:"action-btn view-btn", type: "url", url: "/companystaffview?id={id}" },
            { label: "Delete", class:"action-btn delete-btn", type: "api", endpoint: "/api//item-delete/{id}" },
            { label: "User", class:"action-btn user-btn", type: "url", url: "/" }
            
        ]
    })
});

function renderStaffTable(data) {
    if (!window.DynamicTableRenderer) {
        console.error("DynamicTableRenderer is not loaded");
        return;
    }
    window.DynamicTableRenderer.render(data, staffColumnConfig, "dynamic-data-table");

    setTimeout(() => {
        const rows = document.querySelectorAll("#dynamic-table-body tr");
        rows.forEach(row => attachUserClick(row));

        // Add navigation for View button
        document.querySelectorAll("button.view-btn[data-href]").forEach(btn => {
            btn.addEventListener("click", function () {
                window.location.href = btn.getAttribute("data-href");
            });
        });
    }, 0);
}

function attachUserClick(row) {
    const actionCell = row.querySelector("td:last-child");
    if (!actionCell) return;
    const originalHTML = actionCell.innerHTML;

    const userBtn = actionCell.querySelector("button.view-btn:last-child");
    if (!userBtn) return;

    userBtn.addEventListener("click", () => {
        console.log("User button clicked for row:", row);
        actionCell.innerHTML = `
        <div style="display: flex; flex-direction: row; flex-wrap: wrap; gap: 29px;">
            <label><input type="checkbox" name="permissions"> Request</label>
            <label><input type="checkbox" name="permissions"> Approve</label>
            <label><input type="checkbox" name="permissions"> Purchase</label>
            <label><input type="checkbox" name="permissions"> Users</label>
            <label><input type="checkbox" name="permissions"> Staff</label>
            <label><input type="checkbox" name="permissions"> All</label>
            <button class="view-btn" style="background:#4CAF50;color:#fff;font-weight:bold;padding:6px 22px;border:none;border-radius:5px;">Save</button>
        </div>
        `;
        actionCell.querySelector(".view-btn").addEventListener("click", () => {
            console.log("Save button clicked for row:", row);
            actionCell.innerHTML = originalHTML;
            attachUserClick(row);
        });
    });
}

// Only call this once!
renderStaffTable(staffData);