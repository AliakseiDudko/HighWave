define([ "backbone" ], function(Backbone) {
    var ApiManager = {};

    ApiManager.loadGapi = function() {
        var self = this;

        if (typeof gapi !== "undefined") {
            return this.init();
        }

        require([ "https://apis.google.com/js/client.js?onload=define" ], function() {
            function checkGapi() {
                if (gapi && gapi.client) {
                    self.init();
                } else {
                    setTimeout(checkGapi, 10);
                }
            }

            checkGapi();
        });
    };

    ApiManager.init = function() {
        gapi.client.load("deposits", "v0", function() {
            Backbone.Events.trigger("app:load");
        }, "http://high-wave-595.com/_ah/api");
    };

    return ApiManager;
});