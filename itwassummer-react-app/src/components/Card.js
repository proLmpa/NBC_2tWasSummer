function Card(props) {


  return (
    <div>
      <h1>{props.title}</h1>
      <h3>{props.user}</h3>
    </div>


  )
}

export default Card;