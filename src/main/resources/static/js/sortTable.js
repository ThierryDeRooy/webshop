$(document).ready(function(){
//    sortNameUp = document.getElementById("nameUp");
////    sortNameUp.addEventListener("click", sortTable.bind(null, "productTable", sortNameUp.parentElement.cellIndex, "up", false));
//    sortNameUp.addEventListener("click", sortTable.bind(null, sortNameUp.parentElement.parentElement.parentElement, sortNameUp.parentElement.cellIndex, "up", false));
//    sortNameDown = document.getElementById("nameDown");
//    sortNameDown.addEventListener("click", function(){
////        sortTable("productTable", sortNameDown.parentElement.cellIndex, "down", false);
//        sortTable(sortNameDown.parentElement.parentElement.parentElement, sortNameDown.parentElement.cellIndex, "down", false);
//    });

    sortingElems = document.getElementsByClassName("sortable");
    for (var i = 0; i < sortingElems.length; i++) {
         createSortButton(sortingElems[i], "up", false);
         createSortButton(sortingElems[i], "down", false);
    }
    var sortingElems = document.getElementsByClassName("sortableNum");
    for (var i = 0; i < sortingElems.length; i++) {
         createSortButton(sortingElems[i], "up", true);
         createSortButton(sortingElems[i], "down", true);
    }
});




//function sortTable(tableId, index, direction, nummeric) {
function sortTable(table, index, direction, nummeric) {
  var rows, switching, i, x, y, shouldSwitch, up;
  up = direction.toLowerCase() == "up";
//  table = document.getElementById(tableId);
  switching = true;
  /*Make a loop that will continue until
  no switching has been done:*/
  while (switching) {
    //start by saying: no switching is done:
    switching = false;
    rows = table.rows;
    /*Loop through all table rows (except the
    first, which contains table headers):*/
    for (i = 1; i < (rows.length - 1); i++) {
      //start by saying there should be no switching:
      shouldSwitch = false;
      /*Get the two elements you want to compare,
      one from current row and one from the next:*/
      x = rows[i].getElementsByTagName("TD")[index].innerHTML;
      y = rows[i + 1].getElementsByTagName("TD")[index].innerHTML;
      //check if the two rows should switch place:
      if (nummeric) {
        if (parseFloat(x) > parseFloat(y) && up) {
            shouldSwitch = true;
        } else if (parseFloat(x) < parseFloat(y) && !up) {
            shouldSwitch = true;
        }
      } else if (x > y && up) {
        shouldSwitch = true;
      } else if (x < y && !up) {
        shouldSwitch = true;
      }
      if (shouldSwitch) {
        break;
      }
    }
    if (shouldSwitch) {
      /*If a switch has been marked, make the switch
      and mark that a switch has been done:*/
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
    }
  }
}


function createSortButton(sortTh, direction, numeric) {
//    alert("sortable= " + sortTh.innerHTML);
    var button = document.createElement("BUTTON");
    button.classList.add("sortButton");
    var j = document.createElement("j");
    j.classList.add("arrow");
    j.classList.add(direction);
    button.appendChild(j);
//    return button;
    sortTh.appendChild(button);
    button.addEventListener("click", sortTable.bind(null, sortTh.parentElement.parentElement, sortTh.cellIndex, direction, numeric));
}

