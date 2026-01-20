document.addEventListener("DOMContentLoaded", function () {
  const columnConfig = [
    { header: "Item Name", dataKey: "itemName" },
    { header: "Category", dataKey: "categoryName" },
    { header: "Item Code", dataKey: "itemCode" },
    { header: "Actions" },
  ];

  new TableManager({
    apiEndpoint: "/api/item/get-items",
    idKey: "itemId",
    columnConfig: columnConfig,
    confirmMessage: "Are you sure you want to delete this item?",
    errorMessage: "Failed to delete item: ",
    actions: [
      { label: "Edit", type: "url", url: "/itemedit?id={itemCode}" },
      { label: "View", type: "url", url: "/itemview?id={itemCode}" },
      { label: "Delete", type: "api", endpoint: "/api/item/item-delete/{id}" },
    ],
  });
});
