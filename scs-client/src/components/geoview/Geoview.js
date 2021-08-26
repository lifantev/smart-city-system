import React from 'react';
import './Geoview.css'
import {MapContainer, TileLayer, useMapEvents} from 'react-leaflet'
import 'leaflet/dist/leaflet.css';

function LogCenterZoom() {
    const map = useMapEvents({
        move: () => {
            window.history.pushState(null, null, '/geoview/' + map.getCenter() + ',' + map.getZoom() + 'z')
        },
        zoom: () => {
            window.history.pushState(null, null, '/geoview/' + map.getCenter() + ',' + map.getZoom() + 'z')
        }});
    return null;
}

export function Geoview(){
    return(
        <div className="leaflet-container">
            <MapContainer center={[55.702, 37.53]}
                          zoom={10}
                          scrollWheelZoom={true}>
                <TileLayer
                    attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <LogCenterZoom/>
            </MapContainer>
        </div>
    );
}
