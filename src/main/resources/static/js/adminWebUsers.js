$(document).ready(function(){
    var webusers = document.getElementsByClassName("removeActionForm");
    var usernames = document.getElementsByClassName("removeUsername");
    for (var i = 0; i < webusers.length; i++) {
          var webuser = webusers[i];
          var username = usernames[i];
          webuser.id = username.getAttribute("value")+'-'+i+"-Remove";
              $('#'+webuser.id).on('submit', function() {
                  return confirm('Do you really want to delete the user '+username.getAttribute("value") + ' ?');
              });

    }
});
