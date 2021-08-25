import './App.css';
import {useEffect, useState} from "react";
import axios from "axios";


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
        <div className="App">
            Hello world 2!
        </div> :
        <div>Loading</div>;
}

export default App;
