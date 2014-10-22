define([ "marionette", "text!templates/search-template.html" ], function(Marionette, templateHtml) {
    var SearchView = Marionette.ItemView.extend({
        template: _.template(templateHtml),

        initialize: function() {
            var self = this;

            this.listenTo(this.model, "change", function() {
                this.stickit();
            });

            Backbone.Events.on("gapi:loaded", function() {
                self.search();
            });
        },

        ui: {
            searchButton: "[name=search]",
            amountText: "[name=amount]",
            periodText: "[name=period]",
            currency: "[name=currency]",
            currenciesList: "#currencies"
        },

        events: {
            "click @ui.searchButton": "search",
            "click @ui.currenciesList>li": "changeCurrency"
        },

        bindings: {
            "[name=amount]": {
                observe: "amount",
                onGet: function(value) {
                    return Globalize.formatInt(value);
                },
                onSet: function(value) {
                    return Globalize.parseNumber(value) || null;
                },
                setOptions: {
                    validate: true
                }
            },
            "[name=period]": {
                observe: "period",
                onSet: function(value) {
                    return Globalize.parseNumber(value) || null;
                },
                setOptions: {
                    validate: true
                }
            },
            "[name=currency]": {
                observe: "currency",
                onGet: function(value) {
                    var currency = value;
                    _.each(this.ui.currenciesList.children("li"), function(li) {
                        $(li).removeClass("selected");
                        if ($(li).attr("currency") == currency) {
                            $(li).addClass("selected");
                        }
                    });
                    return currency;
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

        changeCurrency: function(event) {
            var currency = $(event.currentTarget).attr("currency");
            this.model.set("currency", currency, {
                validate: true
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
                    period: this.model.get("period"),
                    currency: this.model.get("currency")
                };
                Backbone.Events.trigger("search:searchQuery", searchQuery);
            } else {
                Backbone.Events.trigger("app:logMessage", "Please enter amount and period.");
            }
        }
    });

    return SearchView;
});