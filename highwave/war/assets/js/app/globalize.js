App.Globalize = function() {
    var culture = "be-BY";// window.navigator.language || "ru-RU";

    var cldr = new Cldr(culture);

    var language = cldr.attributes.language;

    var cldrUrls = [ "assets/js/globalize/cldr/supplemental/likelySubtags.json",
                     "assets/js/globalize/cldr/main/" + language + "/ca-gregorian.json",
                     "assets/js/globalize/cldr/main/" + language + "/numbers.json",
                     "assets/js/globalize/cldr/main/" + culture + "/ca-gregorian.json", 
                     "assets/js/globalize/cldr/main/" + culture + "/numbers.json",
                     "assets/js/globalize/cldr/supplemental/timeData.json",
                     "assets/js/globalize/cldr/supplemental/weekData.json" ];
    _.each(cldrUrls, function(cldrUrl) {
        $.ajax({
            url: cldrUrl,
            success: function(json) {
                Cldr.load($.parseJSON(json));
            },
            async: false
        });
    });

    var globalize = new Globalize(cldr);

    globalize.formatFloat = function(value, fractionDigits) {
        return globalize.formatNumber(value, {
            minimumFractionDigits: fractionDigits,
            maximumFractionDigits: fractionDigits
        });
    };

    globalize.formatIsoDate = function(value) {
        var date = new Date(value);
        return globalize.formatDate(date, {
            pattern: "dd/MM/YY"
        });
    };

    return globalize;
}();