<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <title>TwittMap</title>
    <style>
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;

      }
      #map {
        height: 100%;
      }
    </style>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <link rel="stylesheet" type="text/css" href="public/css/twittMap.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  </head>

<!--dropdown-->
  <nav class="navbar navbar-inverse">
    <div class="container-fluid">
      <div class="navbar-header">
        <p class="navbar-brand"  ><span style="color:#D9006c" >  TwittMap  </span></p>
      </div>
      <ul class="nav navbar-nav">
        <p class="navbar-brand" >       
          <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown">Search 
            <span class="caret"></span></a>
            <ul class="dropdown-menu">
            <!--search box-->
            <li>            
            <div class="row">
            <div id="searchBox" class="span12">
                <form id="custom-search-form" class="form-search form-horizontal pull-left">
                    <div class="input-append span12">
                        <button class="btn btn-danger" type="button" onclick="searchKeyWordFromSeachBox()">
                          <span class=" glyphicon glyphicon-search"></span>
                        </button><input id="input" type="text" class="search-query" placeholder="Search" >
                    </div>
                </form>
            </div>
            </div>
            </li>
            <!--search box-->            
              <li><a onclick ="searchKeyWord('love')">love</a></li>
              <li><a onclick ="searchKeyWord('dinner')">dinner</a></li>
              <li><a onclick ="searchKeyWord('Trump')">Trump</a></li>
              <li><a onclick ="searchKeyWord('I')">I</a></li>
              <li><a onclick ="searchKeyWord('NBA')">NBA</a></li>
              <li><a onclick ="searchKeyWord('Cliton')">Cliton</a></li>
              <li><a onclick ="searchKeyWord('life')">life</a></li>
              <li><a onclick ="searchKeyWord('like')">like</a></li>
              <li><a onclick ="searchKeyWord('real')">real</a></li>
              <li><a onclick ="searchKeyWord('boring')">boring</a></li>
            </ul>
          </li>
          <button id="stopButton" class="btn btn-danger" type="button" onclick="stopSearching()">StopSearching</button>
        </ul>
    </div>
  </nav>

<!--dropdown-->

  <body>

    <div id="map"></div>
    <script>
     
     var markers = [];
     var infowindows = [];
     var center = {lat: 40.8075, lng: -73.962};
     var map;

     function initMap() {
          map = new google.maps.Map(document.getElementById('map'), {
            center: center,
            zoom: 2
          });

          setInterval(function(){getLocations();}, 10000);
     }

     function newMap(origin) {
            map = new google.maps.Map(document.getElementById('map'), {
              center: origin.center,
              zoom: origin.zoom
            });

            setInterval(function(){getLocations();}, 10000);
     }  

     function searchKeyWordFromSeachBox() {
      searchKeyWord(document.getElementById('input').value);
     }
     function searchKeyWord(keyword) {
        clearLocations();
        newMap(map);
        $.ajax({
            url: "/api/keyword/" + keyword,
            type: "GET",
            dataType: 'json',
            error: function(request, status, error) {
                alert('searchKeyWord error');
            },
            success: function(data) {                
                getLocations();
            }
        });
     }
    
     function getLocations() {
        $.ajax({
            url: "/api/queryTweets",
            type: "GET",
            dataType: 'json',
            error: function(request, status, error) {
              alert('getLocation error');
            },
            success: function(results) {                                                
              updateLocationsOnMap(JSON.stringify(results));                
            }
        });
     }

     function updateLocationsOnMap(dataStr) {
        var locations = $.parseJSON(dataStr);       
        for (var i = 0; i < locations.length; i++) {
            updateLocationOnMap(locations[i]);
        }        
     }

     function updateLocationOnMap(tweetJSONObject) {
        var userName = tweetJSONObject.userName;
        var content = tweetJSONObject['text'];
        var location ={lat: tweetJSONObject.lag, lng: tweetJSONObject.longi}; 
             
        var infowindow = new google.maps.InfoWindow({              
          content: '<p>***USER***: ' + userName + '<br />' +
                   '***TWEET***: ' + content + '</p>'
        });
        
        var marker = new google.maps.Marker({
            position:location,
            map:map,
            title:'Click to Show Tweet'
        });
        
        marker.addListener('click', function() {
            infowindow.open(map, marker);
        });
        
        infowindows.push(infowindow);
        markers.push(marker);
     }

     function clearLocations() {
        for (var i = 0; i < infowindows.length; i++) {
            infowindows[i].close();
            infowindows[i] = null;
            markers[i].setMap(null);
            markers[i] = null;
        }

        infowindows = [];
        markers = [];
     }

     function stopSearching() {
        $.ajax({
            url: "/api/stopQuerying",
            type: "PUT",
            dataType: 'json',
            error: function(request, status, error) {
                alert('stopSearching error');
            },
            success: function(data) {                
            }
        });
     }
  
    </script>
    <script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCLqefWais24ATvakmIykoBFXSIUpWGF-4&callback=initMap">
    </script>
  </body>
</html>