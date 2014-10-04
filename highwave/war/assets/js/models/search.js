define([ "backbone" ], function(Backbone) {
    var Search = Backbone.Model.extend({
        defaults: {
            amount: 10000000,
            period: 35,
            currency: "BYR"
        },

        validation: {
            amount: {
                required: true,
                pattern: "number",
                fn: function(value, attr, computedState) {
                    var range;
                    switch (computedState.currency) {
                    case "BYR":
                        range = [ 100000, 1000000000 ];
                        break;
                    case "CUR":
                    case "EUR":
                    case "USD":
                        range = [ 100, 100000 ];
                        break;
                    case "RUB":
                        range = [ 1000, 1000000 ];
                        break;
                    }
                    return Backbone.Validation.validators.range(value, attr, range, this);
                }
            },
            period: {
                required: true,
                pattern: "number",
                range: [ 1, 1000 ]
            },
            currency: {
                oneOf: [ "BYR", "CUR", "USD", "EUR", "RUB" ]
            }
        }
    });

    return Search;
});