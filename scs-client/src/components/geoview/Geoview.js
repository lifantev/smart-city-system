import React, {useEffect, useState} from 'react';
import './Geoview.css'
import {MapContainer, TileLayer, useMapEvents, Polygon, Tooltip, Popup} from 'react-leaflet'
import 'leaflet/dist/leaflet.css';
import {Button, Spin, Typography, Space } from 'antd';
import 'antd/dist/antd.css';
import {GetAxios} from "../GetAxios";
const { Text, Title } = Typography;


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


function DrawAreaDependOnZoom({map}) {
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

        const response = await GetAxios().get(`/api/v1/geo-sharding/config`)
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
                <DrawAreaDependOnZoom map={myMap}/>
                <CreatePopup/>
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
    const purpleOptions = {color: "rgb(24,144,255)"}
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

function CreatePopup(){
    const [pos, setPos] = useState([])
    const map = useMapEvents({
        contextmenu: (e) => {
            const { lat, lng } = e.latlng;
            setPos([lat, lng])
        }}
    );
    if(pos.length != 0) {
        return (
            <div>
                <Popup position={pos}>
                    <Space direction="vertical">
                        <Title level={5} underline style={{color : "rgb(24,144,255)"}}>
                            Current location </Title>
                        <Text style={{ margin : 'auto'}}>x : {pos[0]}</Text>
                        <Text style={{ margin : 'auto'}}>y : {pos[1]}</Text>
                        <Text style={{ margin : 'auto'}}> </Text>
                    </Space>
                    <br/>
                    <Button size="small" type="primary" onClick={() => {ButtonInPopup(pos)}}>Create new object</Button>
                </Popup>
            </div>
        );
    }
    return null;
}

function ButtonInPopup(pos){
    alert(`Here need to be Dima's function. Location is ${pos[0]} : ${pos[1]}`)
}