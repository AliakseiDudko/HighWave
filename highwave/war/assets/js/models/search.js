define([ "backbone" ], function(Backbone) {
    var Search = Backbone.Model.extend({
        defaults: {
            amount: 10000000,
            period: 35
        },

        validation: {
            amount: {
                required: true,
                pattern: "number",
                range: [ 100000, 1000000000 ]
            },
            period: {
                required: true,
                pattern: "number",
                range: [ 1, 1000 ]
            }
        }
    });

    return Search;
});