$(document).ready(function() {
    $(".slider-range-price").slider({
        range: true,
        min: 1,
        max: 50,
        values: [1, 50], // Set initial price range
        slide: function(event, ui) { // Handle slide event
            var min = ui.values[0];
            var max = ui.values[1];


            $('.range-price').text('$' + min + ' - $' + max);
        },
        stop: function(event, ui) {
            var min = ui.values[0];
            var max = ui.values[1];

            var timeoutId = setTimeout(function() {
                // Load the page with new min and max values after 1 second
                window.location.href = "/price?min=" + min + "&max=" + max;
            }, 1000);
        }
    });
    var slider = $(".slider-range-price");
    var min = slider.slider("option", "min");
    var max = slider.slider("option", "max");

    slider.slider("values", 0, min);
    slider.slider("values", 1, max);
    slider.find(".ui-slider-range").css("width", "100%");
    slider.find(".ui-slider-handle").first().css("left", "0%");
    slider.find(".ui-slider-handle").last().css("left", "100%");

});



