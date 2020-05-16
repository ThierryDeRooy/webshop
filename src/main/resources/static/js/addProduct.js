
$(document).ready(function(){
    if (document.getElementById('product.productDetails.stock').value == null || document.getElementById('product.productDetails.stock').value == "") {
        document.getElementById('product.productDetails.stock').value =0;
    }

    var links = document.getElementsByClassName("Clickable");
    var prodCodes = document.getElementsByClassName("ClickCode");
    var prices = document.getElementsByClassName("ClickPrice");
    var btws = document.getElementsByClassName("ClickBtw");
    var transportPoints = document.getElementsByClassName("ClickTransportPoints");
    var units = document.getElementsByClassName("ClickUnit");
    var names = document.getElementsByClassName("ClickName");
    var descriptions = document.getElementsByClassName("ClickDescription");
    var categories = document.getElementsByClassName("ClickCategory");
    var stocks = document.getElementsByClassName("ClickStock");
    var reserveds = document.getElementsByClassName("ClickReserved");
    var photoLocs = document.getElementsByClassName("ClickPhotoLoc");
    var prodDetailsIds = document.getElementsByClassName("ClickProdDetails");
    // For each class "Clickable" inside document
    for (var i = 0; i < links.length; i++) {
      var link = links[i];
      link.id = "modify-"+i;
      prodCodes[i].id = "prodCodeID-"+i;
      prices[i].id = "priceID"+i;
      btws[i].id = "btwID"+i;
      transportPoints[i].id = "transportPointsID"+i;
      units[i].id = "unitID"+i;
      names[i].id = "nameID"+i;
      descriptions[i].id = "descriptionID"+i;
      categories[i].id = "categoryID"+i;
      stocks[i].id = "stockID"+i;
      reserveds[i].id = "reservedID"+i;
      photoLocs[i].id = "photoLocID"+i;
      prodDetailsIds[i].id = "prodDetailsID"+i;
//      link.innerHTML = "MODIFY";
      link.addEventListener("click", modifyProduct);
      //link.onclick = modifyProduct(i);
    }
});

    function modifyProduct(){
        var id = this.id;
        var ctr = id.split("-")[1];
        document.getElementById('prodDetailsId').value = document.getElementById("prodDetailsID"+ctr).getAttribute("value");
        document.getElementById('product.productDetails.code').value = document.getElementById("prodCodeID-"+ctr).innerHTML;
        document.getElementById('product.productDetails.id').value = document.getElementById("prodDetailsID"+ctr).getAttribute("value");
        document.getElementById('product.productDetails.price').value = document.getElementById("priceID"+ctr).innerHTML;
        document.getElementById('product.productDetails.btw').value = document.getElementById("btwID"+ctr).innerHTML;
        document.getElementById('product.productDetails.transportPoints').value = document.getElementById("transportPointsID"+ctr).innerHTML;
        document.getElementById('product.productDetails.unit').value = document.getElementById("unitID"+ctr).getAttribute("value");
        document.getElementById('product.name').value = document.getElementById("nameID"+ctr).innerHTML;
        document.getElementById('product.description').value = document.getElementById("descriptionID"+ctr).getAttribute("value");
        document.getElementById('product.category.id').value = document.getElementById("categoryID"+ctr).getAttribute("value");
        document.getElementById('updateStock').value = "0";
        document.getElementById('product.productDetails.stock').value = document.getElementById("stockID"+ctr).innerHTML;
        document.getElementById('baskets').value = document.getElementById("reservedID"+ctr).innerHTML;
        document.getElementById('btnSubmitId').value = document.getElementById("modify-"+ctr).innerHTML;
        var myImg = document.getElementById("productImagePhoto");
        if (myImg) {
            myImg.setAttribute("src", document.getElementById("photoLocID"+ctr).getAttribute("value"));
        } else {
            var elemImage = document.createElement("img");
            elemImage.setAttribute("src", document.getElementById("photoLocID"+ctr).getAttribute("value"));
            elemImage.setAttribute("class", "product-image");
            elemImage.setAttribute("id", "productImagePhoto");
            document.getElementById('productImage').appendChild(elemImage);
        }
//        selectElement('category.id', document.getElementById("categoryID"+ctr).getAttribute("value"));
    }

$(document).on("change", "#file[type=file]", function () {
    var file = this.files[0];
    if (file.size > 2*1024*1024) {
        alert("Too large Image. Only image smaller than 2MB can be uploaded.");
        $(this).replaceWith('<input class="input-file" type="file">');
        return false;
    }
});

$(function() {
  $("#productTable").tablesorter();
});
$(function() {
  $("#missingProductsTable").tablesorter();
});
