App.CalculatorApp.Search = function() {
    var Search = {};

    var SearchView = Marionette.ItemView.extend({
        template: Backbone.Marionette.TemplateCache.get("search-template"),

        showMessage: function(message) {
            console.log(message);
        },

        initialize: function() {
            _.bindAll(this, "showMessage");
            var self = this;
            App.vent.on("search:searchQuery", self.showMessage("Search started."));
            App.vent.on("search:stop", self.showMessage("Search finished."));
            App.vent.on("search:noSearchTerm", self.showMessage("No search term."));
            App.vent.on("search:noResults", self.showMessage("No results were found."));
        },

        ui: {
            searchButton: "#searchButton",
            amountText: "#amountText",
            periodText: "#periodText"
        },

        events: {
            "click #searchButton": "search",
            "change #amountText": "amountChanged",
            "change #periodText": "periodChanged",
            "keyup #amountText": "amountChanged",
            "keyup #periodText": "periodChanged"
        },

        search: function() {
            if (!this.model.isEmpty()) {
                var searchQuery = {
                    amount: this.model.get("amount"),
                    period: this.model.get("period")
                };
                App.vent.trigger("search:searchQuery", searchQuery);
            } else {
                App.vent.trigger("search:noSearchQuery");
            }
        },

        amountChanged: function() {
            var val = this.ui.amountText.val().trim();
            var amount = val ? parseInt(val) : 0;
            this.model.set("amount", amount);

            if (amount) {
                this.ui.amountText.parent().parent().removeClass("has-error")
            } else {
                this.ui.amountText.parent().parent().addClass("has-error")
            }
        },

        periodChanged: function() {
            var val = this.ui.periodText.val().trim();
            var period = val ? parseInt(val) : 0;
            this.model.set("period", period);

            if (period) {
                this.ui.periodText.parent().parent().removeClass("has-error")
            } else {
                this.ui.periodText.parent().parent().addClass("has-error")
            }
        }
    });

    Search.showSearch = function(search) {
        var searchView = new SearchView({
            model: search
        });
        App.searchRegion.show(searchView);

        searchView.search();
    };

    return Search;
}();