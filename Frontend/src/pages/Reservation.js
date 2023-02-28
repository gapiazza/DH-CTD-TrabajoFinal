import React from 'react'
import { useParams } from 'react-router-dom';
import Footer from '../components/Footer'
import Header from '../components/Header'
import ReservationMain from '../components/ReservationMain'

export default function Reservation() {
  return (
    <div>
        <Header/>
        <ReservationMain/>
        <Footer/>
    </div>
  )
}
