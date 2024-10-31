const myPageService = (() => {
    const getMyFundingList = async (page, memberId, callback) => {

        page = page || 1;
        const response = await fetch(`/members/video/myFunding/${memberId}/${page}`);
        const myFundingPosts = await response.json();

        if(callback){
            callback(myFundingPosts);
        }
    }

    const getFundingBuyerList = async (fundingPostId, callback) => {
        const response = await fetch(`/members/video/myFunding/getBuyerList/${fundingPostId}`);
        const buyersByFundingPostId = await response.json();

        console.log("1: ", getFundingBuyerList);
        console.log("2: ", response);
        console.log("3: ", buyersByFundingPostId);

        if(callback){
            return callback(buyersByFundingPostId);
        }
    }
    return {getMyFundingList: getMyFundingList, getFundingBuyerList: getFundingBuyerList}
})()