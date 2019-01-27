var map;

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {
            lat: 37.4045212,
            lng: -121.9527237
        },
        zoom: 9
    });

    //introduce random shift if stations happens to share the same location
    var knownLocation = [];
    for (var i = 0, keys = Object.keys(locations), ii = keys.length; i < ii; i++) {
        console.log('key : ' + keys[i] + ' val : ' + locations[keys[i]][0]);
        var lat = locations[keys[i]][0];
        var long = locations[keys[i]][1];
        if(knownLocation[lat] === undefined) {
            knownLocation[lat] = long;
        }else{
            locations[keys[i]][1] += 0.0001;
        }
    }

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
        {
            imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m',
        }
    )
}

