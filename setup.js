var map;

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {
            lat: 37.4045212,
            lng: -121.9527237
        },
        zoom: 9
    });

    var markers = locations.map(function (location, i) {
        let marker = new google.maps.Marker({
            position: new google.maps.LatLng(location[0], location[1]),
            label: {
                text: location[3] + ' Watts',
                color: "red",
            },
            icon: image
        });
        var infowindow = new google.maps.InfoWindow({
            content: location[2]
        });
        marker.addListener('click', function () {
            infowindow.open(map, marker);
        });
        return marker;
    });

    var markerCluster = new MarkerClusterer(map, markers,
        {imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'})
}

