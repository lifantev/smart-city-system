import './App.css';
import React, {useEffect, useState} from "react";
import axios from "axios";

import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import {Geoview} from './components/geoview/Geoview.js';

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
                </Switch>
            </Router>
        </main> :
        <div>Loading</div>;
}

export default App;
