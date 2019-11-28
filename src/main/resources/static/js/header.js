
$(document).ready(function(){
  $('.dropdown-submenu a.dropdownsub').on("click", function(e){
    $(this).next('ul').toggle();
    e.stopPropagation();
    e.preventDefault();
  });
});

    function setCode(id, value) {
        var el_code = document.getElementById(id);
        el_code.value = value;

    }

