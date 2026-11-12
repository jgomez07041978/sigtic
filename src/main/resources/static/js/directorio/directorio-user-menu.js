(function () {
    "use strict";

    if (!document.getElementById("directoryUserMenu")) return;
    if (typeof llamarEye === "function") llamarEye();

    var modal = document.getElementById("idModalCambiarPassword");
    if (!modal) return;
    function clearPassword() {
        document.getElementById("idFormPassword").reset();
        if (typeof jQuery === "function") {
            var validator = jQuery("#idFormPassword").data("validator");
            if (validator) validator.resetForm();
        }
        ["10", "11", "12"].forEach(function (number, index) {
            var field = document.getElementById(["contraseniaActual", "contraseniaNueva", "repetirContraseniaNueva"][index]);
            field.value = "";
            field.type = "password";
            field.classList.remove("is-valid", "is-invalid");
            document.getElementById("verPassword" + number).value = "0";
            var icon = document.getElementById("id-fa-eye" + number);
            icon.classList.remove("fa-eye");
            icon.classList.add("fa-eye-slash");
        });
    }
    modal.addEventListener("show.bs.modal", clearPassword);
    modal.addEventListener("hidden.bs.modal", clearPassword);
}());
