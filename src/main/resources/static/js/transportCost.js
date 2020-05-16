
$(document).ready(function(){
    var links = document.getElementsByClassName("Clickable");
    var countries = document.getElementsByClassName("ClickCountry");
    var points = document.getElementsByClassName("ClickPoints");
    var costPrices = document.getElementsByClassName("ClickCostPrice");
    var vats = document.getElementsByClassName("ClickVat");
    var ids = document.getElementsByClassName("ClickId");
    // For each class "Clickable" inside document
    for (var i = 0; i < links.length; i++) {
      var link = links[i];
      link.id = "modify-"+i;
      countries[i].id = "countryID-"+i;
      points[i].id = "pointsID"+i;
      costPrices[i].id = "costPriceID"+i;
      vats[i].id = "vatID"+i;
      ids[i].id = "idID"+i;
      link.addEventListener("click", modifyProduct);
      //link.onclick = modifyProduct(i);
    }
});

    function modifyProduct(){
        var id = this.id;
        var ctr = id.split("-")[1];
        document.getElementById('id').value = document.getElementById("idID"+ctr).getAttribute("value");
        document.getElementById('country').value = document.getElementById("countryID-"+ctr).getAttribute("value");
        document.getElementById('points').value = document.getElementById("pointsID"+ctr).innerHTML;
        document.getElementById('costPrice').value = document.getElementById("costPriceID"+ctr).innerHTML;
        document.getElementById('vat').value = document.getElementById("vatID"+ctr).innerHTML;
    }


$(function() {
  $("#transportCostTable").tablesorter();
});
