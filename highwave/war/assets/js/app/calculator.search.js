App.CalculatorApp.Search = function() {
    var Search = {};

    var SearchView = Marionette.ItemView.extend({
        template: Marionette.TemplateCache.get("search-template"),

        showMessage: function(message) {
            console.log(message);
        },

        initialize: function() {
            _.bindAll(this, "showMessage");
            var self = this;
            App.vent.on("search:searchQuery", function() {
                self.showMessage("Search started.");
            });
            App.vent.on("search:stop", function() {
                self.showMessage("Search finished.");
            });
            App.vent.on("search:noSearchTerm", function() {
                self.showMessage("No search term.");
            });
            App.vent.on("search:noResults", function() {
                self.showMessage("No results were found.");
            });
        },

        ui: {
            searchButton: "[name=search]",
            amountText: "[name=amount]",
            periodText: "[name=period]"
        },

        events: {
            "click @ui.searchButton": "search"
        },

        bindings: {
            "[name=amount]": {
                observe: "amount",
                onGet: function(value) {
                    return value;
                },
                onSet: function(value) {
                    return parseInt(value);
                },
                setOptions: {
                    validate: true
                }
            },
            "[name=period]": {
                observe: "period",
                onGet: function(value) {
                    return value;
                },
                onSet: function(value) {
                    return parseInt(value);
                },
                setOptions: {
                    validate: true
                }
            }
        },

        onRender: function() {
            this.stickit();
            Backbone.Validation.bind(this, {
                forceUpdate: true,
                valid: this.valid,
                invalid: this.invalid
            });
        },

        valid: function(view, attr, selector) {
            var $el = view.$("[" + selector + "=" + attr + "]");
            $el.closest(".form-group").removeClass("has-error");
        },

        invalid: function(view, attr, error, selector) {
            var $el = view.$("[" + selector + "=" + attr + "]");
            $el.closest(".form-group").addClass("has-error");
        },

        search: function() {
            if (this.model.isValid(true)) {
                var searchQuery = {
                    amount: this.model.get("amount"),
                    period: this.model.get("period")
                };
                App.vent.trigger("search:searchQuery", searchQuery);
            } else {
                App.vent.trigger("search:noSearchQuery");
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