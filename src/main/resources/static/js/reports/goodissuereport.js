const girData = [
    { date: "2025-06-25", grn: "0001", branch: "Kathmandu", requestedTo: "John Doe", remarks: "All items checked", status: "Approved" },
    { date: "2025-06-24", grn: "0002", branch: "Pokhara", requestedTo: "Jane Smith", remarks: "Awaiting inspection", status: "Pending" },
    { date: "2025-06-23", grn: "0003", branch: "Biratnagar", requestedTo: "Michael Brown", remarks: "Damaged items", status: "Rejected" },
    // ... (rest of your data)
    { date: "2025-06-06", grn: "0020", branch: "Pokhara", requestedTo: "Sujata Basnet", remarks: "All good", status: "Approved" }
];

// Global filtered data
let filteredData = [...girData];

document.addEventListener("DOMContentLoaded", function () {
    const columnConfig = [
        { header: "Date", dataKey: "date", resizable: true, sortable: true },
        { header: "GRN", dataKey: "grn", resizable: true, sortable: true },
        { header: "Branch", dataKey: "branch", resizable: true, sortable: true },
        { header: "Requested To", dataKey: "requestedTo", resizable: true, sortable: true },
        { header: "Remarks", dataKey: "remarks", resizable: true },
        { header: "Status", dataKey: "status", resizable: true, sortable: true },
        { header: "Actions", resizable: true }, // You can customize this later

        {
            header: "Approval",
            renderCell: (item, _, idx) => {
                if (item.status === "Pending") {
                    return `
                        <button class="btn btn-success approve" onclick="handleApprove(${idx})">Approve</button>
                        <button class="btn btn-danger reject" onclick="handleReject(${idx})">Reject</button>
                    `;
                }
                return '';
            },
            resizable: true
        }
    ];

    // Since we're using static data, we don't use API-based TableManager
    // Instead, we render directly using DynamicTableRenderer
    renderTable(filteredData);

    // Optional: Define approval handlers (you can connect to backend later)
    window.handleApprove = function(idx) {
        const realIndex = girData.findIndex(item => item === filteredData[idx]);
        if (realIndex !== -1) {
            girData[realIndex].status = "Approved";
            filteredData[idx].status = "Approved"; // Update filtered view
            renderTable(filteredData);
            // TODO: Call API to save approval
        }
    };

    window.handleReject = function(idx) {
        const realIndex = girData.findIndex(item => item === filteredData[idx]);
        if (realIndex !== -1) {
            girData[realIndex].status = "Rejected";
            filteredData[idx].status = "Rejected";
            renderTable(filteredData);
            // TODO: Call API to save rejection
        }
    };
});

function filterData() {
    // Always start fresh from original data
    filteredData = [...girData];

    const dateFrom = document.getElementById('filterDateFrom')?.value;
    const dateTo = document.getElementById('filterDateTo')?.value;
    const branch = document.getElementById('branch')?.value;
    const issuedTo = document.getElementById('issuedTo')?.value.trim().toLowerCase();
    const status = document.getElementById('status')?.value;
    const search = document.getElementById('generalSearch')?.value.trim().toLowerCase();

    // Apply filters sequentially
    if (dateFrom) {
        filteredData = filteredData.filter(r => r.date >= dateFrom);
    }
    if (dateTo) {
        filteredData = filteredData.filter(r => r.date <= dateTo);
    }
    if (branch && branch !== "" && branch !== "All") {
        filteredData = filteredData.filter(r => r.branch === branch);
    }
    if (issuedTo) {
        filteredData = filteredData.filter(r => 
            r.requestedTo && r.requestedTo.toLowerCase().includes(issuedTo)
        );
    }
    if (status && status !== "" && status !== "Select Status") {
        filteredData = filteredData.filter(r => r.status === status);
    }
    if (search) {
        filteredData = filteredData.filter(r =>
            (r.date || "").toLowerCase().includes(search) ||
            (r.grn || "").toLowerCase().includes(search) ||
            (r.branch || "").toLowerCase().includes(search) ||
            (r.requestedTo || "").toLowerCase().includes(search) ||
            (r.remarks || "").toLowerCase().includes(search) ||
            (r.status || "").toLowerCase().includes(search)
        );
    }

    renderTable(filteredData);
}

function renderTable(data) {
    if (!window.DynamicTableRenderer) {
        console.error("DynamicTableRenderer is not loaded");
        return;
    }

    // Always render from static data (not API)
    window.DynamicTableRenderer.render(data, columnConfig);
}

// Event Listeners
document.querySelector('.custom-btn-search')?.addEventListener('click', function (e) {
    e.preventDefault();
    filterData();
});

document.getElementById('generalSearch')?.addEventListener('input', filterData);

document.querySelector('.custom-btn-reset')?.addEventListener('click', () => {
    document.getElementById('filterDateFrom').value = '';
    document.getElementById('filterDateTo').value = '';
    if (document.getElementById('branch')) document.getElementById('branch').selectedIndex = 0;
    document.getElementById('issuedTo').value = '';
    if (document.getElementById('status')) document.getElementById('status').selectedIndex = 0;
    document.getElementById('generalSearch').value = '';

    filteredData = [...girData];
    renderTable(filteredData);
});

// Initial render
renderTable(filteredData);