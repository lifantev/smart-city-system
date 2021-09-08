import React, {useEffect, useState} from 'react';
import './Geoview.css'
import {MapContainer, TileLayer, useMapEvents, Polygon, SVGOverlay, MapConsumer, Tooltip} from 'react-leaflet'
import 'leaflet/dist/leaflet.css';
import {Spin} from 'antd';
import 'antd/dist/antd.css';
import axios from "axios";


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


function MyComponent({map}) {
    const[myZoom, setMyZoom] = useState()
    const geomap = useMapEvents({
        zoom: () => {
            setMyZoom(geomap.getZoom())
        }
    });
    if(myZoom >= 10 && myZoom <= 15 ) {
        return (<div>
                <DrawArea map={map}/>
            </div>
        )
    }
    return null;
}

export function Geoview(){

    const [myMap, setMyMap] = useState(new Map());
    const [isLoaded, setLoaded] = useState(false);
    let mainArray = []

    const fetchAreas = async function () {

        const response = await getAxios().get(`/api/v1/geo-sharding/config`)
            .then(res => res.data).then(function (value){

            for (const valueElement of value) {
                for (const valueElementElement of valueElement.positions) {
                    let x = valueElementElement.x
                    let y = valueElementElement.y
                    mainArray.push([x,y])
                }

                setMyMap(myMap.set(valueElement.name, mainArray.slice()))
                mainArray = []

            }

        })
    }

    useEffect(
        () => {
            fetchAreas().then(function (){
                setLoaded(true)
            })

        },
        []
    )


    return( isLoaded ?
        <div className="leaflet-container">
            <MapContainer center={[55.789524469975824, 37.59292603936047]}
                          zoom={10}
                          scrollWheelZoom={true}>
                <TileLayer
                    attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <LogCenterZoom/>
                <MyComponent map={myMap}/>
            </MapContainer>

        </div> :
            <div  style={{
                background: "rgba(44, 44, 44, 0.1)",
                zIndex: "1000",
                width:"100wh",
                height:"100vh",
                backgroundColor : "transparent"
            }}>
                <Spin style={{
                    position: "absolute",
                    top: "50%",
                    left: "50%",
                    transform: "translate(-50%, -50%)",
                    zIndex: "1000"
                }}/>
            </div>

    );

}

function DrawArea({map}) {
    let names = Array.from( map.keys() );
    const purpleOptions = {color: 'purple'}
    const RenderProject = () => {
        let array = [];
        for (let i = 0; i < names.length; i++) {

        array.push(
                <div>
                    <Polygon pathOptions={purpleOptions} positions={map.get(names[i])}>
                        <Tooltip direction="bottom" offset={[0, 20]} opacity={1}>
                            {names[i]}
                        </Tooltip>
                    </Polygon>
                </div>
        )

        }
        return array;
    };

    return(
        <div>
            <RenderProject/>
        </div>
    )
}


function getAxios(){
    let str = localStorage.getItem('scs-properties')
    const jsonstr = JSON.parse(str)
    return axios.create({
        baseURL : jsonstr.scs_coordinator_protocol + "://" + jsonstr.scs_coordinator_host + ":" + jsonstr.scs_coordinator_port,
        responseType : "json",
        headers :{
            "Access-Control-Allow-Credentials" : true
        }
    })
}







