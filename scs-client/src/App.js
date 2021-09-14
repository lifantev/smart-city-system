import './App.css';
import React, {useEffect, useState} from "react";
import axios from "axios";

import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import {Geoview} from './components/geoview/Geoview.js';
import {Spin} from "antd";
import Inventory from './components/inventory/Inventory.js';

function App() {

    const [isLoaded, setLoaded] = useState(false);

    useEffect(
        () => {
            axios.get("/properties.json").then(
                (data) => {
                    localStorage.setItem("scs-properties", JSON.stringify(data.data))
                    setLoaded(true)
                }
            )
        },
        [])

    return isLoaded ?
        <main>
            <Router>
                <Switch>
                    <Route path="/geoview" component={Geoview} />
                    <Route path="/inventory" component={Inventory}/>
                </Switch>
            </Router>
        </main> :
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
}

export default App;
