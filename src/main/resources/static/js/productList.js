            function sortNameUp(selector, children) {
                $(selector).children(children).sort(function(a, b) {
                    var A = $(a).find('.sortName').text().toUpperCase();
                    var B = $(b).find('.sortName').text().toUpperCase();
                    return (A < B) ? -1 : (A < B) ? 1 : 0;
                }).appendTo(selector);
            }

            function sortNameDown(selector, children) {
                $(selector).children(children).sort(function(a, b) {
                    var A = $(a).find('.sortName').text().toUpperCase();
                    var B = $(b).find('.sortName').text().toUpperCase();
                    return (A > B) ? -1 : (A > B) ? 1 : 0;
                }).appendTo(selector);
            }

            function sortPriceUp(selector, children) {
                $(selector).children(children).sort(function(a, b) {
                    var A = $(a).find('.price').text();
                    var B = $(b).find('.price').text();
                    return (A < B) ? -1 : (A < B) ? 1 : 0;
                }).appendTo(selector);
            }

            function sortPriceDown(selector, children) {
                $(selector).children(children).sort(function(a, b) {
                    var A = $(a).find('.price').text();
                    var B = $(b).find('.price').text();
                    return (A > B) ? -1 : (A > B) ? 1 : 0;
                }).appendTo(selector);
            }


$(document).ready(function(){
    $('#NameSortUp').click(function() {
        sortNameUp("div.prodList", "div.sortItem");
    });
    $('#NameSortDown').on('click', function() {
        sortNameDown("div.prodList", "div.sortItem");
    });
    $('#PriceSortUp').on('click', function() {
        sortPriceUp("div.prodList", "div.sortItem");
    });
    $('#PriceSortDown').on('click', function() {
        sortPriceDown("div.prodList", "div.sortItem");
    });
});

$(document).ready(function(){
    $('#pageNr').on('change', function() {
        var frm = document.getElementById('pageNrForm');
        frm.submit();
    });
    $('#pageNrDown').on('change', function() {
        var frm = document.getElementById('pageNrFormDown');
        frm.submit();
    });
    $('#pageSizeSelect').change(function() {
        var frm = document.getElementById('pageSizeForm');
        frm.submit();
    });
});



