(function () {
    "use strict";

    var toggle = document.getElementById("directorySidebarToggle");
    var sidebar = document.getElementById("sidebar");
    var backdrop = document.getElementById("directorySidebarBackdrop");
    if (!toggle || !sidebar || !backdrop) return;

    var desktop = window.matchMedia("(min-width: 1200px)");
    var wasDesktop = desktop.matches;
    var header = document.querySelector(".directory-header");
    var current = document.getElementById("sidebarDirectorio");
    sidebar.setAttribute("aria-label", "Menú principal");
    if (current) {
        current.classList.remove("collapsed");
        current.setAttribute("aria-current", "page");
    }

    function update() {
        var toggled = document.body.classList.contains("toggle-sidebar");
        var open = desktop.matches ? !toggled : toggled;
        toggle.setAttribute("aria-expanded", String(open));
        backdrop.hidden = desktop.matches || !open;
        sidebar.inert = !open;
    }

    function closeMobile() {
        if (!desktop.matches) {
            document.body.classList.remove("toggle-sidebar");
            update();
        }
    }

    function sizeHeader() {
        if (header) document.body.style.setProperty("--directory-header-height", header.getBoundingClientRect().height + "px");
    }

    toggle.addEventListener("click", function () {
        document.body.classList.toggle("toggle-sidebar");
        update();
    });
    backdrop.addEventListener("click", function () {
        closeMobile();
        toggle.focus();
    });
    sidebar.addEventListener("click", function (event) {
        var link = event.target.closest("a");
        if (link && link.getAttribute("href") !== "#") closeMobile();
    });
    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape" && !document.querySelector(".modal.show")
                && !desktop.matches && document.body.classList.contains("toggle-sidebar")) {
            closeMobile();
            toggle.focus();
        }
    });
    function resize() {
        if (wasDesktop !== desktop.matches) {
            document.body.classList.remove("toggle-sidebar");
            wasDesktop = desktop.matches;
        }
        sizeHeader();
        update();
    }
    window.addEventListener("resize", resize);
    if (typeof ResizeObserver !== "undefined" && header) new ResizeObserver(sizeHeader).observe(header);
    sizeHeader();
    update();
}());
