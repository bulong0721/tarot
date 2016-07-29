const webpack = require('webpack');

module.exports = {
    //页面入口文件配置
    entry: {
        app:'./index.js',
        base: ["vue","vue-router","vue-resource"],
    },
    //入口文件输出配置
    output: {
        path: "dist",
        filename: '[name].js'
    },
    devServer: {
        contentBase: 'dist',
        devtool: 'eval',
        hot: false,
        inline: true,
        port: 3001,
        host: 'localhost',
    },
    resolve: {
        extensions: ['', '.js']
    },
    module: {
        //加载器配置
        loaders: [
            {test: /\.js?$/,loader: 'babel',include: __dirname,query: {presets: ['es2015']}},
            {test: /\.vue$/,loader: 'vue'},
            {test: /\.css$/, loader: 'style!css'},
            //{test: /\.(png|jpg)$/, loader: 'url-loader?limit=8192'},
        ]
    },
    plugins: [
        new webpack.BannerPlugin('This file is created by mckay'),
        new webpack.optimize.CommonsChunkPlugin("base", "base.js"),
        new webpack.optimize.UglifyJsPlugin({
            output: {
                comments: false,
            },
            compress: {
                warnings: false
            }
        }),
        new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': '"production"'
            }
        })
    ]
}