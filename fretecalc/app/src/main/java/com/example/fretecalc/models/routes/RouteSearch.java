package com.example.fretecalc.models.routes;

import androidx.room.Embedded;

public class RouteSearch {
    @Embedded
    private RouteResponse routeResponse;
    @Embedded
    private Search search;

    public RouteSearch(RouteResponse routeResponse, Search search) {
        this.routeResponse = routeResponse;
        this.search = search;
    }

    public RouteResponse getRouteResponse() {
        return routeResponse;
    }

    public void setRouteResponse(RouteResponse routeResponse) {
        this.routeResponse = routeResponse;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
}
