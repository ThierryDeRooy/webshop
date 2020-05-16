
$(document).ready(function(){
  $('.dropdown-submenu a.dropdownsub').on("click", function(e){
    $(this).next('ul').toggle();
    e.stopPropagation();
    e.preventDefault();
  });

  var logoutAct = document.getElementById("logoutID");
  if (logoutAct!=null)
    logoutAct.addEventListener("click", formSubmit);
});

    function setCode(id, value) {
        var el_code = document.getElementById(id);
        el_code.value = value;

    }

    function formSubmit() {
    	document.getElementById("logoutForm").submit();
    }


