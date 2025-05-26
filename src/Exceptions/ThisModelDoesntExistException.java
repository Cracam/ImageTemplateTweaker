/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author Camille LECOURT
 */
public class ThisModelDoesntExistException extends Exception{
          public ThisModelDoesntExistException() {
        super();
    }

    public ThisModelDoesntExistException(String string) {
        super(string);
    }
}