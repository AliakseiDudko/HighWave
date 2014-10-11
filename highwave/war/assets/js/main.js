require.config({
    baseUrl: "assets/js",
    paths: {
        jquery: "common/jquery.min",
        underscore: "common/underscore.min",
        bootstrap: "../bootstrap/js/bootstrap.min",
        cldr: "globalize/cldr/cldr",
        backbone: "backbone.marionette/backbone.min",
        stickit: "backbone.marionette/backbone.stickit.min",
        validation: "backbone.marionette/backbone.validation.min",
        marionette: "backbone.marionette/backbone.marionette.min"
    },
    shim: {
        underscore: {
            exports: "_"
        },
        backbone: {
            exports: "Backbone",
            deps: [ "jquery", "underscore" ]
        },
        stickit: {
            exports: "Backbone.Stickit",
            deps: [ "backbone" ]
        },
        validation: {
            exports: "Backbone.Validation",
            deps: [ "backbone" ]
        },
        marionette: {
            exports: "Backbone.Marionette",
            deps: [ "backbone", "stickit", "validation", "bootstrap" ]
        }
    },
    deps: [ "jquery", "underscore" ]
});

require([ "app/app", "app/globalize" ], function(App, Globalize) {
    this.Globalize = Globalize;

    App.start();

    require([ "app/gapi" ], function(ApiManager) {
        ApiManager.loadGapi();
    });
});