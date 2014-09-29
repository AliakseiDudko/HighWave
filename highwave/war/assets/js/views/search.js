define([ "marionette", "app/globalize", "text!templates/search-template.html" ], function(Marionette, Globalize, templateHtml) {
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
            periodText: "[name=period]"
        },

        events: {
            "click @ui.searchButton": "search"
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
                Backbone.Events.trigger("search:searchQuery", searchQuery);
            } else {
                Backbone.Events.trigger("search:noSearchQuery");
            }
        }
    });

    return SearchView;
});