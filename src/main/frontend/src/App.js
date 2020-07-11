import React, { useState, useEffect, useCallback } from "react";
import "./App.css";
import axios from "axios";
import { useDropzone } from "react-dropzone";

const UserProfiles = () => {
  const [userProfiles, setUserProfiles] = useState([]); //initially an empty array of user profiles
  const fetchUserProfiles = () => {
    axios.get("http://localhost:8080/api/v1/user-profile").then(res => {
      console.dir(res);
      const data = res.data;
      setUserProfiles(data);
    });
  };

  useEffect(() => {
    fetchUserProfiles();
  }, []);
  return userProfiles.map((userProfile, index) => {
    return (
      <div  className="userprofile" key={index}>
        <UserProfileImage userProfileId={userProfile.profileId} 
                        userProfileImageHash={userProfile.profileImageHash} />        
        <br />
        <br />
        <h1>{userProfile.username}</h1>
        <p>{userProfile.profileImageHash}</p>
        <MyDropzone userProfileId={userProfile.profileId} />
        <br />
      </div>
    );
  });
};

function UserProfileImage({userProfileId, userProfileImageHash}){
  return (userProfileId ? <img src={`http://localhost:8080/api/v1/user-profile/${userProfileId}/image/download?hash=${userProfileImageHash}`} alt="User profile"/> : null);
}

function MyDropzone({ userProfileId }) {
  const onDrop = useCallback(acceptedFiles => {
    const file = acceptedFiles[0];
    console.dir(file);

    const formData = new FormData();
    formData.append("userProfileImage", file);
    axios
      .post(
        `http://localhost:8080/api/v1/user-profile/${userProfileId}/image/upload`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data"
          }
        }
      )
      .then((res) => {
        let hash = res.data;
        console.dir(hash);
      })
      .catch(err => {
        console.error(err);
      });
  }, [userProfileId]);
  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {isDragActive ? (
        <p>Drop the image here ...</p>
      ) : (
        <p>
          Drag 'n' drop the profile image, or click to select the profile image
        </p>
      )}
    </div>
  );
}

function App() {
  return (
    <div className="App">
      <UserProfiles />
    </div>
  );
}

export default App;
