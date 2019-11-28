
$(document).ready(function(){
    var links = document.getElementsByClassName("Clickable");
    var prodCodes = document.getElementsByClassName("ClickCode");
    var prices = document.getElementsByClassName("ClickPrice");
    var units = document.getElementsByClassName("ClickUnit");
    var names = document.getElementsByClassName("ClickName");
    var descriptions = document.getElementsByClassName("ClickDescription");
    var categories = document.getElementsByClassName("ClickCategory");
    var states = document.getElementsByClassName("ClickStatus");
    var photoLocs = document.getElementsByClassName("ClickPhotoLoc");
    // For each class "Clickable" inside document
    for (var i = 0; i < links.length; i++) {
      var link = links[i];
      link.id = "modify-"+i;
      prodCodes[i].id = "prodCodeID-"+i;
      prices[i].id = "priceID"+i;
      units[i].id = "unitID"+i;
      names[i].id = "nameID"+i;
      descriptions[i].id = "descriptionID"+i;
      categories[i].id = "categoryID"+i;
      states[i].id = "statusID"+i;
      photoLocs[i].id = "photoLocID"+i;
//      link.innerHTML = "MODIFY";
      link.addEventListener("click", modifyProduct);
      //link.onclick = modifyProduct(i);
    }
});

    function modifyProduct(){
        var id = this.id;
        var ctr = id.split("-")[1];
        document.getElementById('product.productDetails.code').value = document.getElementById("prodCodeID-"+ctr).innerHTML;
        document.getElementById('product.productDetails.price').value = document.getElementById("priceID"+ctr).innerHTML;
        document.getElementById('product.productDetails.unit').value = document.getElementById("unitID"+ctr).getAttribute("value");
        document.getElementById('product.name').value = document.getElementById("nameID"+ctr).innerHTML;
        document.getElementById('product.description').value = document.getElementById("descriptionID"+ctr).innerHTML;
        document.getElementById('product.category.id').value = document.getElementById("categoryID"+ctr).getAttribute("value");
        document.getElementById('product.productDetails.status').value = document.getElementById("statusID"+ctr).getAttribute("value");
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
