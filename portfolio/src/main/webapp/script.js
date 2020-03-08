// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

function removeDefaultCommentText() {
  commentInput = document.getElementById('comment-input');
  commentInput.innerText = '';
  commentInput.style.color = 'rgba(0, 0, 0)';
}

function addDefaultCommentText() {
  document.getElementById('comment-input').innerText = 'Add a comment...';
  commentInput.style.color = 'rgba(0, 0, 0, .5)';
}

async function loadAllPost() {
  const response = await fetch('/comment');
  const comments = await response.json();
  addAllPostToBlog(comments);
}

function addAllPostToBlog(comments) {
  const blogContainer = document.getElementById('blog-container');

  // comments sorted by most recently posted to resemble a blog
  for (const commentObject of comments) {
    blogContainer.append(
        createPost(commentObject.author, commentObject.comment));
  }
}

function addPostToBlog() {
  const blogContainer = document.getElementById('blog-container');
  const author = document.getElementById('author-input').value;
  const comment = document.getElementById('comment-input').value;
  blogContainer.prepend(createPost(author, comment));
}

function createPost(author, comment) {
  const postContainer = document.createElement('div');
  postContainer.append(createPostUserImage());
  postContainer.append(createPostComment(author, comment));
  return postContainer;
}

function createPostUserImage() {
  const userImage = document.createElement('img');
  userImage.src = 'images/anonymous.png';
  return userImage;
}

function createPostComment(author, comment) {
  const commentContainer = document.createElement('div');
  commentContainer.className = 'user-comment';
  addCommentText(commentContainer, 'h3', author);
  addCommentText(commentContainer, 'p', comment);
  return commentContainer;
}

function addCommentText(commentContainer, elementType, content) {
  const element = document.createElement(elementType);
  element.innerText = content;
  commentContainer.append(element);
}

function addComment(clickSubmitEvent) {
  clickSubmitEvent.preventDefault();

  fetch('/comment', {
    method: 'post',
    body: new URLSearchParams(
        new FormData(document.getElementById('comment-form')))
  })
      .then(addPostToBlog)
      .then(resetForm);
}

function resetForm() {
  document.getElementById('comment-form').reset();
}

async function showCommentFormIfLoggedIn() {
  const response = await fetch('/authentication');
  const authenticationInfo = await response.json();
  if (authenticationInfo.isLoggedIn) {
    resetForm();
  } else {
    hideCommentForm();
  }
}

function hideCommentForm() {
  document.getElementById('comment-form').style.display = 'none';
}

document.addEventListener('DOMContentLoaded', () => {
  showCommentFormIfLoggedIn()
  loadAllPost();

  document.getElementById('comment-input')
      .addEventListener('focus', removeDefaultCommentText);
  document.getElementById('comment-input')
      .addEventListener('blur', addDefaultCommentText);
  document.getElementById('comment-form-submit')
      .addEventListener('click', addComment);
});