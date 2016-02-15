'use strict';

module.exports = {

    entry: {
        input: './build/relay-babel-input.js'
    },
    output: {
        path: __dirname + '/build',
        filename: 'relay-babel-output.js'
    },
    module: {
        loaders: [
            {
                test: /\.js$/,
                loader: 'babel',
                query: {stage: 0, plugins: ['./build/babelRelayPlugin']}
            }
        ]
    }

};