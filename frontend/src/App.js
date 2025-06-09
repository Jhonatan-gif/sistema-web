import React, { useState } from 'react';

function App() {
  const [email, setEmail] = useState('');
  const [ruc, setRuc] = useState('');
  const [placa, setPlaca] = useState('');
  const [datos, setDatos] = useState(null);

  const consultarDatos = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/consultar?ruc=${ruc}&placa=${placa}`);
      const data = await response.json();
      setDatos(data);
    } catch (error) {
      console.error('Error al consultar:', error);
    }
  };

  return (
    <div className="p-6">
      <h1>Consulta SRI y ANT</h1>
      <input type="email" placeholder="Correo" value={email} onChange={e => setEmail(e.target.value)} />
      <input type="text" placeholder="RUC o Cédula" value={ruc} onChange={e => setRuc(e.target.value)} />
      <input type="text" placeholder="Placa del vehículo" value={placa} onChange={e => setPlaca(e.target.value)} />
      <button onClick={consultarDatos}>Consultar</button>
      {datos && (
        <pre>{JSON.stringify(datos, null, 2)}</pre>
      )}
    </div>
  );
}

export default App;
