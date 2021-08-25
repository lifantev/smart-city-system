import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import {Geoview} from './components/geoview/Geoview.js';

function App() {
    return (
        <main>
            <Router>
            <Switch>
                <Route path="/geoview" component={Geoview} />
            </Switch>
            </Router>
        </main>
    );
}

export default App;