$(function() {
  $("#itemsTable").tablesorter();
});
$(function() {
  $("#ordersTable").tablesorter();
});
$(document).ready(function(){
    $('#deleteFormId').on('submit', function() {
        return confirm('Do you really want to delete this order?');
    });
});
