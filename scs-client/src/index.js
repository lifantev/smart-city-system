import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';

class Doc extends React.Component{
    componentDidMount(){
        document.title = "Smart city"
    }

    render(){
        return null;
    }
}

ReactDOM.render(
  <React.StrictMode>
      <Doc/>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);

