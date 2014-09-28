define([ "backbone" ], function(Backbone) {
    var ApiManager = {};

    onLoadGapi = function() {
        Backbone.Events.trigger("app:load");
    };

    init = function() {
        // var root = "http://localhost:8888/_ah/api";
        var root = "https://high-wave-595.appspot.com/_ah/api";

        gapi.client.load("deposits", "v0", onLoadGapi, root);
    };

    checkGapi = function checkGapi() {
        if (gapi && gapi.client) {
            init();
        } else {
            setTimeout(checkGapi, 10);
        }
    };

    ApiManager.loadGapi = function() {
        var self = this;

        if (typeof gapi !== "undefined") {
            return init();
        }

        require([ "https://apis.google.com/js/client.js?onload=define" ], checkGapi);
    };

    return ApiManager;
});