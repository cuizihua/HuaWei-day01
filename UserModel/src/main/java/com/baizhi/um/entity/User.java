package com.baizhi.um.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
 private Integer id;
 private String name;
 private int sex;
 private String password;
 private Date birthDay;
 private String photo;
 private String email;
 //getter and setter and constructor and toString...


 @Override
 public String toString() {
  return "User{" +
          "id=" + id +
          ", name='" + name + '\'' +
          ", sex=" + sex +
          ", password='" + password + '\'' +
          ", birthDay=" + birthDay +
          ", photo='" + photo + '\'' +
          ", email='" + email + '\'' +
          '}';
 }

 public User(Integer id, String name, int sex, String password, Date birthDay, String photo, String email) {
  this.id = id;
  this.name = name;
  this.sex = sex;
  this.password = password;
  this.birthDay = birthDay;
  this.photo = photo;
  this.email = email;
 }

 public User() {
 }

 public Integer getId() {
  return id;
 }

 public void setId(Integer id) {
  this.id = id;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public int getSex() {
  return sex;
 }

 public void setSex(int sex) {
  this.sex = sex;
 }

 public String getPassword() {
  return password;
 }

 public void setPassword(String password) {
  this.password = password;
 }

 public Date getBirthDay() {
  return birthDay;
 }

 public void setBirthDay(Date birthDay) {
  this.birthDay = birthDay;
 }

 public String getPhoto() {
  return photo;
 }

 public void setPhoto(String photo) {
  this.photo = photo;
 }

 public String getEmail() {
  return email;
 }

 public void setEmail(String email) {
  this.email = email;
 }
}